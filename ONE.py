import os
def traversal(path):
    dir=os.listdir(path)
    if dir is None:
        exit(1)
    else:
        for each in dir:
            file_path = path + os.path.sep + each
            if os.path.isdir(file_path):
                traversal(file_path)
            else:
                if each == 'dog.txt' and os.path.isfile(file_path):
                    print("find dog.txt at "+ file_path)
traversal(raw_input("Please input dir path:"))