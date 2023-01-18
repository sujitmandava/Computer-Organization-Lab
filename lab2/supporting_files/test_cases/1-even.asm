	.data
n:
	8
l:
	2
	-1
	7
	5
	3
	4
	-6
	2
	.text
main:
	add %x0, %x0, %x3
	add %x0, %x0, %x10
	load %x0, $n, %x9
loop:
	load %x3, $l, %x4
	addi %x3, 1, %x3
	andi %x4, 1, %x5
	beq %x5, 0, even
	jmp check
even:
	slt %x4, %x0, %x6
	beq %x6, 0, success
	jmp check
success:
	addi %x10, 1, %x10
	jmp check
check:
	blt %x3, %x9, loop
	beq %x3, %x9, eop
eop:
	end
