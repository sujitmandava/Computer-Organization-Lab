	.data
count:
	0
	0
	0
	0
	0
	0
	0
	0
	0
	0
	0
marks:
	2
	3
	0
	5
	10
	7
	1
	10
	10
	8
	9
	6
	7
	8
	2
	4
	5
	0
	9
	1
n:
	20
	.text
main:
	add %x0, %x0, %x3
	load %x0, $n, %x5
	subi %x5, 1, %x5
loop:
	load %x3, $marks, %x4
	load %x4, $count, %x6
	addi %x6, 1, %x6
	store %x6, $count, %x4
	addi %x3, 1, %x3
	bgt %x3, %x5, endl
	jmp loop
endl:
	end
