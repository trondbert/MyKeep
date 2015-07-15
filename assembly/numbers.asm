
global int_to_str

extern print_bytes
extern print_ln
extern print_num
 
section .text

int_to_str: ; rdi holder tallet, rsi peker til område det skal skrives til (min 64 bit), 
			; rdx er ønsket antall siffer (paddes med '0' på venstre side)
	jmp 	int_to_str_push
	int_to_str_pushed:
	mov 	r12, rdi ; tallet som vi skal lage en string for (base 10)
	mov 	r13, rsi ; hvor strengen skal plasseres
	mov 	r14, rdx ; antall tegn vi skal padde ut til
	zeros_to_desired_width:
	mov 	r15, 0x3030303030303030
	push 	r15
	mov 	rdi, r13
	mov 	rsi, rsp
	mov 	rcx, r14
	rep 	movsb
	pop 	r15

	mov 	r9, 10	; r9 er tierpotens som skal ganges med ti til input-tallet er mindre
	mov 	rcx, 1
	nofDigits:
	cmp 	r12, r9
	jl		nofDigitsFound
	mov 	rax, r9		; --- r9 *= 10
	mov 	rdx, 10
	mul 	rdx
	mov 	r9, rax 	; ---
	inc 	rcx
	jmp 	nofDigits
	nofDigitsFound:
	mov 	r9,  r14         ; hvor mange bytes blir det, som oftest blir det vel r14.
	cmp 	rcx, r14
	jle		actualWidthFound
	mov 	r9,  rcx		 ; rcx > r14, r9 = max(rcx, r14)
	actualWidthFound:
	add 	r13,  r9		; lets start with the zero to terminate the number string
	mov 	rdi, r13
	mov 	rdx, 0
	push 	rdx
	mov 	rsi, rsp
	movsb
	pop 	rdx
	dec 	r13		 		; points at position of least significant digit
	new_round:
	mov 	rax, r12
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; no fractions
	div 	rbx     ; rax / rbx -> rax
	mov 	r12, rax ; r12 = r12 / 10, rdx = rest ved divisjon (= siste siffer) 
	write_remainder_to_memory:
	or  	rdx, 0x30 	; rdx, now ready for print
	mov 	rdi, r13		; to
	push 	rdx
	mov 	rsi, rsp		; from
	movsb 					; move byte
	pop 	rdx
	back_to_new_round:
	cmp		r12, 0
	jz	 	int_to_str_exit
	dec 	r13
	jmp 	new_round
	int_to_str_push:
	push 	 r9
	push 	r11
	push 	r12
	push 	r13
	push 	r14
	push 	r15
	jmp 	int_to_str_pushed
	int_to_str_exit:
	pop 	r15
	pop 	r14
	pop 	r13
	pop 	r12
	pop 	r11
	pop 	 r9
	ret

section	.data

	testMsg: dw 'foofdssa', 0