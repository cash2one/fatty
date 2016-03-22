import os
def traversal(A_path,b_path):
    dir=os.listdir(A_path)
    print(dir)
    for each in dir:
        if each == "dog.txt":
            print("dog.txt"+"at"+"A_path" + "b_patch")
if __name__ == '__main__':
	A_path = "D:\\"
	b_path = "C"
	traversal(A_path,b_path)
    

