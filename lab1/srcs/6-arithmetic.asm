	.data
a:
	1
d:
	3
n:
	7
	.text
main:
	addi %x0, 1, %x3
	addi %x0, 65535, %x7
	load %x0, $a, %x4
	load %x0, $d, %x5
	load %x0, $n, %x6
loop:
	store %x4, $a, %x7
	add %x4, %x5, %x4
	subi %x6, 1, %x6
	subi %x7, 1, %x7
	beq %x0, %x6, endl
	jmp loop
endl:
	end
