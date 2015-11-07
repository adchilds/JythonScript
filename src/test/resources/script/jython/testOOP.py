import sys

from script.jython.oop.test import SomeClass

if __name__ == '__main__':
    someClass = SomeClass()
    result = someClass.compute(sys.argv[1], sys.argv[2])