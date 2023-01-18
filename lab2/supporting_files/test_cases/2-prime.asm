	.data
a:
	2
	.text
main:
	load %x0, $a, %x1
	divi %x1, 2, %x3
	addi %x0, 2, %x4
	beq %x1, %x4, P
loop:
	div %x1, %x4, %x5
	beq %x31, %x0, NP
	addi %x3, 1, %x3
	bgt %x4, %x3, P
	jmp loop
P:
	addi %x0, 1, %x10
	end
NP:
	subi %x0, 1, %x10
	end		
