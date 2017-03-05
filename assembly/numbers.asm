
global int_to_str
global mod
global sqrt
global str_to_int

extern print_raw
extern print_bytes
extern print_ln
extern print_num
extern print_num_ln

section .text

int_to_str: ; rdi holder tallet, rsi peker til område det skal skrives til (min 64 bit), 
			; rcx er ønsket antall siffer (paddes med '0' på venstre side)

	mov 	r13, rdi ; tallet som vi skal lage en string for (base 10)
	mov 	r14, rsi ; hvor strengen skal plasseres
	mov 	r15, rcx ; antall tegn vi skal padde ut til
    mov     rdi, r13
	zeros_to_desired_width:
    mov     rdi, r14
    mov 	rax, 0x3030303030303030
	push 	rax
	mov 	rsi, rsp
	mov 	rcx, r15
    cld
	rep     movsb
	pop 	rax

	mov 	r12, 10	; r12 er tierpotens som skal ganges med ti til input-tallet er mindre
	mov 	rcx, 1
	nofDigits:
	cmp 	r13, r12
	jl		nofDigitsFound
	mov 	rax, r12		; --- r12 *= 10
	mov 	rdx, 10
	mul 	rdx
	mov 	r12, rax 	;     ---
	inc 	rcx
	jmp 	nofDigits
	nofDigitsFound:
	mov 	r12,  r15         ; r12 holder nå antall digits
    cmp 	rcx, r15
	jle		actualWidthFound
	mov 	r12,  rcx		 ; rcx > r15, r12 = max(rcx, r15)
	actualWidthFound:
	add 	r14,  r12		; lets start with the zero to terminate the number string
    mov 	rdi, r14
    mov     rax, 0
	push 	rax
	mov 	rsi, rsp
	movsb
	pop 	rax
	dec 	r14      		; points at position of least significant digit
	new_round:
	mov 	rax, r13
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; no fractions
	div 	rbx     ; rax / rbx -> rax
	mov 	r13, rax ; r13 = r13 / 10, rdx = rest ved divisjon (= siste siffer) 
    mov     rdi, rdx
	write_remainder_to_memory:
	add  	rdx, 0x30 	    ; rdx, now ready for print
	mov 	rdi, r14		; to
	push 	rdx
	mov 	rsi, rsp		; from
	movsb 					; move byte
    dec     r14
	pop 	rdx
	back_to_new_round:
	cmp		r13, 0
	jz	 	int_to_str_exit ; if we have written all digits
	dec 	r15
	jmp 	new_round
	int_to_str_exit:
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
