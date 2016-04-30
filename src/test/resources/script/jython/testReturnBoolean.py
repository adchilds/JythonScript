import sys


def determineTruthiness(a, b):
    """
    Determines if either of the two given arguments are True.

    :param a: the first parameter to check
    :param b: the second parameter to check
    :return: whether or not at least one value is True
    """
    return a or b


if __name__ == '__main__':
    # Set the defaults
    a = False
    b = False

    # If arguments were passed to this script, use those
    try:
        a = sys.argv[1]
        b = sys.argv[2]
    except Exception:
        pass

    # Determine if any of the given arguments are True
    result = determineTruthiness(a, b)