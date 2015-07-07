;nasm -f macho64 hello_3.asm
;gcc -m64 -mmacosx-version-min=10.4 -isysroot /Developer/SDKs/MacOSX10.4u.sdk -o hello_3 hello_3.o
;gcc -m64 -mmacosx-version-min=10.5 -isysroot /Developer/SDKs/MacOSX10.5.sdk -o hello_3 hello_3.o

SECTION .data
hello		db 'syscall: hello, world', 10,0
hellolen 	equ $ - hello
hello2 		db 'puts: hello, world', 10,0
hellolen2 	equ $ - hello2
hello3 		db 'printf: hello, world %d', 10,0
hellolen3 	equ $ - hello3

SECTION .text
	GLOBAL start
	extern _printf, _puts

;align 4, db 0x90
start:

;	push	qword rbp				; build stack frame
;	mov		rbp, rsp

	push 	rbp
	lea		rdi, [rel hello3]	; lea works with yasm but not nasm
	;mov		rdi, qword hello3		; pointer to string to be displayed
	xor		rax, rax				; there are 0 float values being passed
	mov		rsi, 42					; rsi is the first variable to display.  Just giving it a number
	call	_printf
	pop 	rbp

;puts test
;	lea		rdi,  [hello2 wrt rip]	; lea works with yasm but not nasm
;	mov		rdi, qword hello2		; string to be displayed
;	call 	_puts

; syscall write to device test
;   mov rax, 0x2000004    ; sys_write
;   mov rdi, 1            ; stdout
;   mov rsi, qword hello  ; string
;   mov rdx, hellolen     ; length
;   syscall

	mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall