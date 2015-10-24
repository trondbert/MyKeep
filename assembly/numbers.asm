
global int_to_str
global mod
global sqrt
global str_to_int

extern print_bytes
extern print_ln
extern print_num
extern print_num_ln

section .text

int_to_str: ; rdi holder tallet, rsi peker til område det skal skrives til (min 64 bit), 
			; rdx er ønsket antall siffer (paddes med '0' på venstre side)
	jmp 	int_to_str_push
	int_to_str_pushed:
	mov 	r14, rdi ; tallet som vi skal lage en string for (base 10)
	mov 	r15, rsi ; hvor strengen skal plasseres
	mov 	r14, rdx ; antall tegn vi skal padde ut til
	zeros_to_desired_width:
	mov 	r15, 0x3030303030303030
	push 	r15
	mov 	rdi, r15
	mov 	rsi, rsp
	mov 	rcx, r14
	rep 	movsb
	pop 	r15

	mov 	r13, 10	; r13 er tierpotens som skal ganges med ti til input-tallet er mindre
	mov 	rcx, 1
	nofDigits:
	cmp 	r14, r13
	jl		nofDigitsFound
	mov 	rax, r13		; --- r13 *= 10
	mov 	rdx, 10
	mul 	rdx
	mov 	r13, rax 	; ---
	inc 	rcx
	jmp 	nofDigits
	nofDigitsFound:
	mov 	r13,  r14         ; hvor mange bytes blir det, som oftest blir det vel r14.
	cmp 	rcx, r14
	jle		actualWidthFound
	mov 	r13,  rcx		 ; rcx > r14, r13 = max(rcx, r14)
	actualWidthFound:
	add 	r15,  r13		; lets start with the zero to terminate the number string
	mov 	rdi, r15
	mov 	rdx, 0
	push 	rdx
	mov 	rsi, rsp
	movsb
	pop 	rdx
	dec 	r15		 		; points at position of least significant digit
	new_round:
	mov 	rax, r14
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; no fractions
	div 	rbx     ; rax / rbx -> rax
	mov 	r14, rax ; r14 = r14 / 10, rdx = rest ved divisjon (= siste siffer) 
	write_remainder_to_memory:
	or  	rdx, 0x30 	; rdx, now ready for print
	mov 	rdi, r15		; to
	push 	rdx
	mov 	rsi, rsp		; from
	movsb 					; move byte
	pop 	rdx
	back_to_new_round:
	cmp		r14, 0
	jz	 	int_to_str_exit
	dec 	r15
	jmp 	new_round
	int_to_str_push:
	push 	 r13
	push 	r13
	push 	r14
	push 	r15
	push 	r14
	push 	r15
	jmp 	int_to_str_pushed
	int_to_str_exit:
	pop 	r15
	pop 	r14
	pop 	r15
	pop 	r14
	pop    	r13
	pop 	 r13
	ret

mod:    ; rdi in , rax out
    push    rdx
    push    rcx
    mov     rax, rdi
    mov     rdx, 0
    div     rsi
    mov     rax, rdx
    pop     rcx
    pop     rdx
    ret

sqrt:      ;rdi in , rax out
    mov     r12, 1   ; min
    mov     r14, rdi ; max
    mov     r15, rdi
    push    r12                 ; best candidate
    new_run:
        mov     rax, r12
        add     rax, r14
        mov     rdx, 0
        mov     rcx, 2
        div     rcx         ; rax = avg(min, max)
        mov     r13, rax
        mov     rdi, r12
                                        ;push    rax
                                        ;call    print_num_ln
                                        ;mov     rdi, r13
                                        ;call    print_num_ln
                                        ;mov     rdi, r14
                                        ;call    print_num_ln
                                        ;pop     rax
        mul     rax         ; rax = sqr(avg)
        cmp     rax, r15
        jle     take_top_half  ; candidate squared <= number

        mov     rax, r13
        sub     rax, 1
        mov     r14, rax ; max = candidate - 1
        jmp     exit_decision

        take_top_half:
            pop     rcx
            push    r13                 ; best candidate
            mov     rax, r13
            add     rax, 1
            mov     r12, rax            ; min = candidate + 1; candidate might be the best integer approx.

        exit_decision:
            cmp     r13, r14            ; diff between candidate and max
            jnz     new_run

        pop     rax
    ret

str_to_int: ; rdi in, rax out ;=============================
    jmp     push_em

    the_start:          ;--------------------------
    mov     r14, 0       ; r14 will hold result
    mov     rsi, rdi    ; will read string bytes from rsi, rdi is the argument

    new_byte_str_to_int: ;--------------------------
    mov     rax, 0
    lodsb               ; read from rsi and shift rsi to the right
    mov     r12, rax
    cmp     r12, 0
    jz      end_str_to_int

    push    rsi
    mov     rax, 10
    mul     r14
    mov     r14, rax     ; existing number multiplied by 10
    sub     r12, 0x30    ; character is converted from ascii to number
    add     r14, r12     ; added current digit, ready for next round
    pop     rsi
    jmp     new_byte_str_to_int

    end_str_to_int:     ;--------------------------
    mov     rax, r14

    pop    rdi
    pop    rsi
    pop    r12
    pop    r14
    jmp     the_end

    push_em:            ;--------------------------
    push    r14
    push    r12
    push    rsi
    push    rdi
    jmp     the_start

    the_end:
    ret

section	.data

	testMsg:    dw      'foofdssa', 0
    one:        equ     1
    two:        equ     2
