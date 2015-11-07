from java.lang import System

class SomeClass:
    """

    """


    def __init__(self):
        """

        :return:
        """
        self.delta = 7

        # Test some Jython functionality
        System.out.println('Object initialized...')


    def compute(self, x, y):
        """

        :rtype: object
        :param x:
        :param y:
        :return:
        """
        x = x + self.delta
        y = y - self.delta

        return x * y
