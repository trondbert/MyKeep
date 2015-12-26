; /usr/local/bin/nasm -f macho64 printing.asm && ld -macosx_version_min 10.7.0 -lSystem -o printing64 printing.o && ./printing64
; /usr/local/bin/nasm -f macho64 printing.asm && ld -macosx_version_min 10.7.0 -lSystem -o printing64 printing.o && ./printing64

global start_printing
global print_char
global print_ln
global print_num
global print_bytes
global print_str_zeroterm
global print_strlen
global print_bytes_ln
global print_num_ln
 
section .text

print_bytes: ; rdi holder bytes som skal skrives, rsi holder antall bytes
	push 	rdi
	push 	rsi
	mov     rax, 0x2000004 ; write
    mov     rdi, 1 ; stdout
    mov		rsi, rdi ; hva skal skrives
    mov 	rdx, rsi ; antall bytes
    syscall
    pop 	rsi
    pop 	rdi
    ret

print_char: ; rdi holder characteren som skal skrives
    push    r11
    push    rdi
	mov 	rsi, rsp ; Setter rsi til stack-pointer-adresse. Peker altså til opprinnelig rsi-verdi som sist ble pusha på stack
	mov     rax, 0x2000004 ; write
    mov     rdi, 1 ; stdout
    mov     rdx, 1 ; antall bytes
    syscall
    pop     rdi
    pop     r11
    ret

print_str_zeroterm: ; rdi peker på strengen
	push 	rdi
	mov 	r10, rdi ; r10 skal økes og peke på aktiv char inn i strengen
	new_round:
		mov 	rax, 0   ; vi leser byte for byte inn i nedre del av rax, så vi nullstiller hele rax
		mov 	rsi, r10 ; lodsb leser fra plasseringen [rsi]
		lodsb	
		cmp		rax, 0
		jz 		exit_zeroterm

		mov 	rdi, rax
		call 	print_char
		add 	r10, 1
		jmp		new_round

	exit_zeroterm:
		pop 	rdi
	ret

print_strlen: ; rdi peker på strengen
	mov 	r14, 0    ; counter
	mov 	r15, rdi  ; address
	mov 	rsi, rdi
	loadbytes:
		lodsq
		mov 	r10, rax 					; string bytes , 8 bytes at a time
		mov 	r13, 0x00000000000000FF		; byte mask
	loopstart:
		mov 	rax, r10
		and 	rax, r13					; pick current byte
		cmp		rax, 0 						; end of string?
		jz		finish

		inc		r14						    ; count chars
		shl 	r13, 8  					; shift mask 8 bits to the left
		cmp 	r13, 0						; shifted out of 64 bit range?
		jnz 	loopstart
	add 	r15, 8							; target next 8 byte chunk
	mov 	rsi, r15                        ; setup argument for loading more bytes from memory
	jmp 	loadbytes

	finish:
	mov 	rax, r14
	ret

print_ln: 
	push 	rdi
	mov 	rdi, 10
	call 	print_char
	pop		rdi
	ret

print_num:
    call    print_num_rec
    ret

print_num_rec:
    push    rdi
	mov 	rax, rdi
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; no fractions
	div 	rbx     ; rax / rbx -> rax
	cmp		rax, 0
	jz	 	exit_print_num_rec

	push 	rdx
	mov 	rdi, rax
	call 	print_num_rec
	pop		rdx
	
	exit_print_num_rec:
	mov 	rdi, rdx
	or  	rdi, 0x30 	; rdx, now ready for print
	call 	print_char
	pop    rdi
	ret

print_num_ln:
    call    print_num
    call    print_ln
    ret

print_bytes_ln:
    call    print_bytes
    call    print_ln
    ret

section .data
 
msg:    db      "Hello", 10
.len:   equ     $ - msg
fadd 	st1

exclamate: 	db	33
buf64bit: 	db 0x0000000000000000

