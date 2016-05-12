# -*- coding: utf-8 -*-
import os
import re
import json
import tempfile
import logging
import zipfile_patch
import requests
import subprocess
import StringIO
import sys
from argparse import ArgumentParser
import commands
from datetime import datetime, MINYEAR

#target_store = u"百度手机助手pc网页版"
# target_store = u"360"
# target_store = u"百度"
#user = "houdini@intel.com"

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)
sh = logging.StreamHandler()
logger.addHandler(sh)

parser = ArgumentParser(usage = "filter.py [--appstore target_store --user user@intel.com]", description = "App store apk list filter")
parser.add_argument("--user", dest = "user", help = "user email for help")
parser.add_argument("--appstore", dest = "appstore", help = "target store for help")
args = parser.parse_known_args(sys.argv)
target_store = unicode(args[0].appstore, encoding = 'utf-8')
user = args[0].user

if not (user and target_store):
    logger.error(commands.getoutput("python filter.py --help"))
    exit(1)

# server_hostname = r"ubuntu-dev-jason-ji.sh.intel.com"
server_hostname = r"shssgdpd047.sh.intel.com"
rest_ranks = r"http://" + server_hostname + r"/apk/ranks"
rest_rank = r"http://" + server_hostname + r"/apk/rank/%s"
rest_app = r"http://" + server_hostname + r"/apk/%d/download"
proxies = {""}


def aapt_dump_badging(apk_path):
    proc = subprocess.Popen(['./aapt', 'dump', 'badging', apk_path], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = proc.communicate()
    lines = []
    if proc.returncode != 0:
        return None
    for line in iter(StringIO.StringIO(out).readline, ''):
        lines.append(line)
    return lines

def aapt_list(apk_path):
    proc = subprocess.Popen(['./aapt', 'list', apk_path], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = proc.communicate()
    lines = []
    if proc.returncode != 0:
        return None
    for line in iter(StringIO.StringIO(out).readline, ''):
        lines.append(line)
    return lines


def is_arm_bin(bin_path):
    proc = subprocess.Popen(['readelf', '-h', bin_path], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = proc.communicate()
    lines = []
    if proc.returncode != 0:
        logger.error("failed to open readelf")
        return False
    for line in iter(StringIO.StringIO(out).readline, ''):
        if re.search(r"Machine", line):
            if re.search(r"[aA][rR][mM]", line):
                return True
    return False


def contains_arm_elf(apk_path, dir_):
    logger.info(apk_path)
    with zipfile_patch.ZipFile(apk_path, "r") as zf:
        for name in zf.namelist():
            if not re.search(dir_, name): continue
            with zf.open(name) as f:
                with tempfile.NamedTemporaryFile() as tf:
                    tf.write(f.read())
                    if is_arm_bin(tf.name):
                        logger.info("arm elf found: %s,%s", name, apk_path)
                        return True
    return False


def x86_mixed(apk_path):
    return contains_arm_elf(apk_path, r'lib/x86')

def x86_missing(apk_path, libs_3rd_party_file):
    lines = aapt_list(apk_path)
    if lines is None:
        return False

    libs_3rd_party = set()
    with open(libs_3rd_party_file, "r") as f:
        for lib in f:
            libs_3rd_party.add(lib.strip())
    libs_arm = set()
    libs_x86 = set()
    arm_lib_pattern = r"lib/armeabi-v7a"
    arm_v7 = False
    for line in lines:
        if re.search(arm_lib_pattern, line):
            arm_v7 = True
            break
    if not arm_v7:
        arm_lib_pattern = r"lib/armeabi/"
    for line in lines:
        if re.search(arm_lib_pattern, line):
            libs_arm.add(os.path.basename(line.strip()))
        elif re.search(r"lib/x86/", line):
            libs_x86.add(os.path.basename(line.strip()))
    # print "------3rd party------"
    # print libs_3rd_party
    # print "------arm------"
    # print libs_arm
    # print "------x86------"
    # print libs_x86
    return _x86_missing(libs_arm, libs_x86, libs_3rd_party)


def _x86_missing(libs_arm, libs_x86, libs_3rd_party):
    if len(libs_arm) == 0: return False
    found_user_lib_in_x86 = False
    c = 0
    for lib in libs_arm:
        if lib in libs_3rd_party:
            c += 1
            if lib not in libs_x86:
                return True
        else: # user lib
            if (not found_user_lib_in_x86) and lib in libs_x86:
                found_user_lib_in_x86 = True
    if c == len(libs_arm):
        return False # all arm libs are 3rd party libs
    else:
        return not found_user_lib_in_x86


def apk_to_be_tested(apk_path, libs_3rd_party):
    lines = aapt_dump_badging(apk_path)
    if lines is None:
        return False
    for line in lines:
        if re.match(r"native-code", line):
            # NDK
            if re.match(r"[xX]86", line): # NDK/x86 + arm
                return x86_mixed(apk_path) or x86_missing(apk_path, libs_3rd_party)
            else:
                return True # NDK/arm
    # Dalvik
    return contains_arm_elf(apk_path, r'assets/')


def create_custom_app_list(name, desc, email, password, md5sums):
    logger.info("creating a new custom app list")
    resp = requests.post(
        r"http://" + server_hostname + r"/api/app/custom-list/create",
        json.dumps({
            'user_email': email,
            'list_name': name,
            'list_description': desc,
            'app_md5sums': md5sums
        }),
        proxies=None
    )
    logger.info(resp.content)
    assert resp.status_code == 200, "Wrong data POSTed to web server!"


# --------- Main -----------
sess = requests.session()
resp = sess.post(rest_ranks, proxies=None)
ranks = json.loads(resp.content)
crash_list = [ "b8da57306d06e16d245e42a8db6a4ae5", "04b6ea9228d99861bc375a7eaad9311f", "6f50869445753fa51b53074a70c6e746", "81ba6ceba2bd7230dfda4e63378f9837"]

ancient = datetime(MINYEAR, 1, 1)
latest = ancient
for r in ranks:
    if r["app_store"] != target_store:
        continue
    dt = datetime.strptime(r["datetime"], r"%Y-%m-%d %H:%M:%S")
    if dt > latest:
        latest = dt

if latest is ancient:
    logger.error("Nothing updated, please use last update")
    exit(0)

logger.info("latest update is %s" % str(latest))

target_ranks = []
for r in ranks:
    if r["app_store"] != target_store:
        continue
    dt = datetime.strptime(r["datetime"], r"%Y-%m-%d %H:%M:%S")
    if dt != latest:
        continue
    target_ranks += [r]

logger.info("Found these target ranks:")
logger.info(target_ranks)

app_ids = set()
apps = []
for r in target_ranks:
    resp = sess.post(rest_rank % r["id"], proxies=None)
    resp_apps = json.loads(resp.content)
    for a in resp_apps:
        if int(a["app_id"]) in app_ids:
            continue
        app_ids.add(int(a["app_id"]))
        apps += [a]

# print "Found these apps:"
# print apps

apk_cache_dir = "./apps"
target_apps = []
ignore_apps = []
for a in apps:
    m = a["md5"]
    apk_path = apk_cache_dir + os.sep + m[0:2] + os.sep + m[2:4] + os.sep + m + ".apk"
    if not os.path.exists(apk_path):
        resp = sess.get(rest_app % int(a["app_id"]), stream=True)
        if resp.status_code != 200:
            logger.warning( "Warning: failed to download apk: %s" % m)
            continue
        if not os.path.exists(os.path.dirname(apk_path)):
            os.makedirs(os.path.dirname(apk_path))
        with open(apk_path, r"w") as f:
            for data in resp.iter_content(1024 * 1024):
                f.write(data)
    if apk_to_be_tested(apk_path, "ThirdPartySO.txt"):
        logger.info("Apk need test")
        if (m == crash_list[0]) or (m == crash_list[1]) or (m == crash_list[2]) or (m == crash_list[3]):
            logger.info("Crash apk: %s" % m)
            continue 
        target_apps += [m]
    else:
        logger.info("Apk ignored by filter")
        ignore_apps += [m]

if len(target_apps) == 0:
    logger.error("All apps were filtered. None matches Houdini's options...")
    exit(0)

logger.info("A list of these apps will be created:")
logger.info(target_apps)
with open("houdini_prc.txt","w")as f:
    for n in range(len(target_apps)):
    	f.write(target_apps[n] + '\n')
with open("houdini_prc_ignore.txt","w")as df:
    for j in range(len(ignore_apps)):
        df.write(ignore_apps[j] + '\n')

date = datetime.now()

create_custom_app_list(
    "[Baidu][Selected]%s" % date.strftime(r"%Y-%m-%d %H:%M:%S"),
    "PRC 3000 more apk list no need of full ported x86 and pure java and crash list",
    user, "xxxxxx", target_apps
)

