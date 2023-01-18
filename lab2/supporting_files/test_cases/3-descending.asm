	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	add %x0, %x0, %x3
	load %x0, $n, %x10
loop:
	load %x3, $a, %x5
	addi %x3, 0, %x4
	jmp sort
sort:
	load %x4, $a, %x6
	bgt %x6, %x5, maxupdate
	beq %x6, %x5, maxupdate
	jmp sortcont
maxupdate:
	add %x6, %x0, %x5
	add %x0, %x4, %x7
	jmp sortcont
sortcont:
	addi %x4, 1, %x4
	blt %x4, %x10, sort
	load %x3, $a, %x8
	store %x5, 0, %x3
	store %x8, 0, %x7
	addi %x3, 1, %x3
	jmp check
check:
	blt %x3, %10, loop
	beq %x3, %x10, eop
eop:
	end
