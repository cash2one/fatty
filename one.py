import os
def traversal(path):
    dir=os.listdir(path)
    print(dir)
    for each in dir:
      if each is None:
        print("Have nothing!")
      elif each == "dog.txt":
          file_path = path + os.path.sep + each + os.path.sep + "dog.txt"
          print("dog.txt"+"at"+ file_path)
      elif each is None:
        break
      else:
        traversal(each)
A_path="c:"
traversal(A_path)
    

