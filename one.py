import os
def traversal(path):
    dir=os.listdir(path)
    if dir is None:
    	exit(1)
    else:
    	for each in dir:
    		if each == "dog.txt":
    			file_path = path + os.path.sep + each + os.path.sep + "dog.txt"
                print("find dog.txt at "+ file_path)
            else:
            	traversal(each)
traversal(raw_input("Please input dir path:"))
