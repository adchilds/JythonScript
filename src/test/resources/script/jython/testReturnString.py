import sys


if __name__ == '__main__':
    # Set the defaults
    a = ''
    b = ''
    
    # If arguments were passed to this script, use those
    try:
        a = sys.argv[1]
        b = sys.argv[2]
    except Exception:
        pass

    # Sets the result to the longer of the two Strings
    result = a if len(a) > len(b) else b