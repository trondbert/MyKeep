; /usr/local/bin/nasm -f macho64 printing.asm && ld -macosx_version_min 10.7.0 -lSystem -o printing64 printing.o && ./printing64

global start_printing
global print_char
global print_ln
global print_num
global print_bytes
global print_str_zeroterm
global print_strlen
 
section .text

print_bytes: ; rdi holder bytes som skal skrives, rsi holder antall bytes
	push 	rdi
	push 	rsi
	mov     rax, 0x2000004 ; write
    mov 	rdx, rsi ; antall bytes
    mov		rsi, rdi ; hva skal skrives
    mov     rdi, 1 ; stdout
    syscall
    pop 	rsi
    pop 	rdi
    ret

print_char: ; rdi holder characteren som skal skrives
	push 	rdi
	push 	rsi
	push 	rdx
	mov 	rsi, rsp ; Setter rsi til stack-pointer-adresse. Peker altså til opprinnelig rsi-verdi som sist ble pusha på stack
	mov     rax, 0x2000004 ; write
    mov     rdi, 1 ; stdout
    mov     rdx, 1 ; antall bytes
    syscall
    pop 	rdx
    pop 	rsi
    pop 	rdi
    ret

print_str_zeroterm: ; rdi peker på strengen
	push 	rdi
	mov 	rdx, rdi ; rdx skal økes og peke på aktiv char inn i strengen
	new_round:
		mov 	rax, 0   ; vi leser byte for byte inn i nedre del av rax, så vi nullstiller hele rax
		mov 	rsi, rdx ; lodsb leser fra plasseringen [rsi]
		lodsb	
		cmp		rax, 0
		jz 		exit_zeroterm

		mov 	rdi, rax
		push 	rdx
		call 	print_char
		pop 	rdx
		add 	rdx, 1
		jmp		new_round

	exit_zeroterm:
		pop 	rdi
	ret

print_strlen: ; rdi peker på strengen
	mov 	rdx, rdi
	call 	print_ln
	;mov 	rcx, 0    ; counter
	;mov 	rsi, rdx
;	push 	rdx
;	lodsq
;	pop 	rdx
;	mov 	rdi, rax
;	mov 	r11, 0x000000000000FF00
;	loopstart:
;		mov 	rdi, 77
;		call 	print_num
;		call 	print_ln
;		mov 	rdi, rdx
;		call 	print_num
;		call 	print_ln
;		mov 	rbx, rdx  		; temp
;		and		rbx, r11
;		mov 	rdi, rbx
;		call 	print_num
;		call 	print_ln
;		cmp		rbx, 0 ; end of string?
;		jmp 		finish
;
;		add		rcx, 1  ; count chars
;		mov 	rax, r11
;		mov 	rdi, rcx
;		call 	print_num
;		call 	print_ln
;		call 	print_ln
;		mov 	r12, 0x100
;		mul		r12  ; shift 1 byte to the left
;		mov 	r11, rax
		;
;		cmp 	rcx, 5
;		jl 		loopstart
;
;	finish:
;		mov 	rdi, 123
;		call	print_num
	ret

print_ln: 
	push 	rdi
	mov 	rdi, 10
	call 	print_char
	pop		rdi
	ret

print_num:	
 	push 	rdx
 	push  	rdi
 	push 	rbx
	mov 	rax, rdi
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; remainder?
	div 	rbx     ; rax / rbx -> rax
	cmp		rax, 0
	jz	 	exit_print_num

	push 	rdx
	mov 	rdi, rax
	call 	print_num
	pop		rdx
	
	exit_print_num:
	mov 	rdi, rdx
	or  	rdi, 0x30 	; rdx, now ready for print
	call 	print_char
	pop 	rbx
	pop     rdi
	pop 	rdx
	ret

start_printing:
	mov     rsi, 0x0a
	call 	print_char
	mov 	rax, 5239
	mov 	rcx, 0
	call 	print_num
	mov     rax, 0x2000004 ; write
    mov     rdi, 1 ; stdout
    mov     rsi, rsp
    mov     rdx, 5
    syscall

	mov     rsi, 0x0a
	call 	print_char

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
 
section .data
 
msg:    db      "Hello", 10
.len:   equ     $ - msg
fadd 	st1

linefeed: db	10

exclamate: 	db	33
