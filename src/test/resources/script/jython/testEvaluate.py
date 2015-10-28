import sys


def multiply(a, b):
    """
    Multiplies two values, 'a' and 'b', together.

    :param a: the first parameter to multiply
    :param b: the second parameter to multiply
    :return: the product of [a * b]
    """
    return a * b


if __name__ == '__main__':
    # Set the defaults
    a = 5
    b = 5

    # If arguments were passed to this script, use those
    try:
        a = sys.argv[1]
        b = sys.argv[2]
    except Exception:
        pass

    # Multiple either (5 * 5) or (a * b [arguments passed in from Java])
    result = multiply(a, b)