# -*- coding: utf-8 -*-
import os
import re
import time

def find_houdini(dir_path):
	print 'Progressing...'
	t = time.strftime('%Y%m%d%H%M%S')
	if os.path.exists(dir_path) is False:
		print "Input unavailable path!"
		exit(1)

	dirs = os.listdir(dir_path)
	for dir in dirs:
		file_path = dir_path + os.path.sep + dir + os.path.sep + "logcat.txt"
		if os.path.exists(file_path) is False:
			continue
		
		with open(file_path, 'rU') as  f:
			i = 0
			find_flag = False
			while True:
				line_content = f.readline()
				if not line_content:
					break
				i = i + 1
				m = re.match(r'.*(houdini).*', line_content)
				if m:
					if re.match(r'.*(Initialize library).*(RELEASE).*(successfully).*', line_content) is None and re.match(r'.*(Added shared library).*(for ClassLoader by Native Bridge).*', line_content) is None:
						if find_flag is False:
							with open(os.getcwd() + os.path.sep + 'result_' + t + '.txt', 'ab+') as fs:
								fs.write('------------------------------------------------------------\r\n');
								fs.write('Find file: ' + file_path + '\r\n');
								fs.write('Find in the folow line(s):' + '\r\n');
						find_flag = True
						with open(os.getcwd() + os.path.sep + 'result_' + t + '.txt', 'ab+') as fs:
							fs.write('\tline number: ' + str(i) + '\r\n')
	print 'Finished!\n'
	if os.path.exists(os.getcwd() + os.path.sep + 'result_' + t + '.txt'):
		print 'Result at ' + os.getcwd() + os.path.sep + 'result_' + t + '.txt'
	else:
		print 'No found any result!'


if __name__ == '__main__':
	dir_path = raw_input("Please input dir path:")
	find_houdini(dir_path)