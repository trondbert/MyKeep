; /usr/local/bin/nasm -f macho64 printing.asm && ld -macosx_version_min 10.7.0 -lSystem -o printing64 printing.o && ./printing64

global start_printing
global print_char
global print_ln
global print_num
global print_bytes
 
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
	mov 	rsi, rsp ; Setter rsi til stack-pointer-adresse. Peker altså til opprinnelig rsi-verdi som sist ble pusha på stack
	mov     rax, 0x2000004 ; write
    mov     rdi, 1 ; stdout
    mov     rdx, 1 ; antall bytes
    syscall
    pop 	rdi
    ret

print_ln: 
	push 	rdi
	mov 	rdi, 10
	call 	print_char
	pop		rdi
	ret

print_num:	
	mov 	rax, rdi
	mov 	rbx, 10 ; divisor
	mov 	rdx, 0  ; remainder?
	div 	rbx     ; rax / rbx -> rax
	
	cmp		rax, 0
	jz	 	exit_print_num

	push 	rdx
	call 	print_num
	pop 	rdx

	exit_print_num:
	mov 	rdi, rdx    ; ready for print
	or  	rdi, 0x30 	; the remainder in ASCII
	call 	print_char
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
