EXTERN print_bytes
EXTERN print_char 
EXTERN print_ln
EXTERN print_num
EXTERN print_strlen
EXTERN _printf

section .data
 
msg:        dw      0x2134, 10 ; -> 0x34 0x20 -> ascii "4!"
.len:       equ     $ - msg

buffer:     db      'Hello, world'
                     times 64 - $ + buffer db ' '
.len:       equ     $ - buffer

numbers:    db      0x37, 0x34
meaning:    equ     42

wordvar:    dw      0x36, 0x38, 0

fmt2 db 'This is a test. %d is the meaning of life.', 10, 0    ; something stupid here
fmt  db 0x36, 0x39, 10, 0    ; something stupid here

section .text

global start
EXTERN _printf 

start:
    
    mov     rdi, fmt
    call    print_strlen
    ;mov     rdi, rax
    ;call    print_num

    ;call    print_ln

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

old:
    add     rsp, 16 ; Skip argc and argv[0]
    pop     rsi
    sub     rbp, 8
    mov     rdi, rsi
    mov     rax, 0
    call    _printf  ; Printer argument
    add     rbp, 8
    mov     rdi, 10
    call    print_char

    ;;;;; printing meaning of life with printf
    sub     rbp, 16 ; 8 bytes x 2 (argument)
    mov     rax, 0
    lea     rdi, [rel fmt] ; RIP-relative
    mov     rsi, meaning
    call    _printf 
    sub     rbp, 16
    
	mov     rdi, msg
    mov     rsi, msg.len
    call 	print_bytes

	mov     rdi, buffer ; Hello world
    mov     rsi, buffer.len
    call 	print_bytes

    mov     rdi, 10
    call    print_char
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
    ret
