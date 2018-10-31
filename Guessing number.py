import random

print("Let's play the game of guessing number!")
range_num = 0
while range_num <= 0:
	range_num = int(input("please enter a positive integer:"))
print("hh")

ans = random.randint(0, range_num + 1)
num = -1
while num != ans:
	num = int(input("Please enter the number you guess:"))
	if num > ans:
		print("The number you guess is too bigger, try littler one!")
	elif num < ans:
		print("The number you guess is too littler, try bigger one!")

print("Gongradulation!")

	