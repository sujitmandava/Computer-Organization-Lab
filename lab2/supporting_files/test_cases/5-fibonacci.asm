	.data
n:
	10
	.text
main:
	add %x0, %x0, %x3
	load %x0, $n, %x4
	subi %x4, 2, %x4
	addi %x0, 0, %x5
	addi %x0, 1, %x6
	addi %x0, 65535, %x10
	store %x5, 0, %x10
	subi %x10, 1, %x10
	store %x6, 0, %x10
	subi %x10, 1, %x10
loop:
	add %x5, %x6, %x7
	addi %x3, 1, %x3
	store %x7, 0, %x10
	subi %x10, 1, %x10
	add %x6, %x0, %x5,
	add %x7, %x0, %x6
	jmp check
check:
	blt %x3, %x4, loop
	beq %x3, %x4, eop
eop:
	end