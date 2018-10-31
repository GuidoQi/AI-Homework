import random

print("There are three numbers, please guess what it is!")
print("Clue:")
print("error: The 3 numbers are not in the mystical numbers.")
print("Only the number is correct:the number is right , but the position is not right.")
print("Absolutely right:numbers is right and the position also right.")

Num_digits = 3
Max_times = 10

def get_digit(num):
	a = int(num / 100)
	b = int(num % 100 / 10)
	c = num % 10
	return a, b, c

ans =  299 #random.randint(100, 999)
a1, b1, c1 = get_digit(ans)
list = [a1, b1, c1]
secret_numbers = 1

def compare(num):
	pass





message = "error"
counter = 1
while message != "Congradulation" or counter < 11:
	num_guess = int(input("This is the "+str(counter)+" time:"))
	message = compare(num_guess)
	print(message)

request = input("Do you want to play again?--Yes or No")

