import sys

if __name__ == '__main__':
    # Set the defaults
    a = 5.5
    b = 3.278

    # If arguments were passed to this script, use those
    try:
        a = sys.argv[1]
        b = sys.argv[2]
    except Exception:
        pass

    # Add the two parameters
    result = a + b