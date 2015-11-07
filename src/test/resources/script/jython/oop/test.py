from java.lang import System

class SomeClass:
    """
    Simple class for unit testing OOP (Object Oriented Programming) with Jython.
    """


    def __init__(self):
        """
        Initializes this object's state.
        """
        self.delta = 7

        # Test some Jython functionality
        System.out.println('Object initialized...')


    def compute(self, x, y):
        """
        Returns the product of a simple computation on the given x and y.
        """
        x = x + self.delta
        y = y - self.delta

        return x * y
