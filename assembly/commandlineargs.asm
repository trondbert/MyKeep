EXTERN print_bytes
EXTERN print_char
EXTERN print_ln 
EXTERN print_num
EXTERN print_str_zeroterm
EXTERN _printf

section .data
 
msg:        dw      0x2134, 10, 0 ; -> 0x34 0x20 -> ascii "4!"
.len:       equ     $ - msg

numbers:    db      0x37, 0x34
meaning:    equ     42

fmt         db      'This is a test', 10, 0    ; something stupid here
fmt2        db      'Message is: %s', 10, 0    ; something stupid here
str1        db      "My string", 0
fmt3        db      'Another test', 10, 0
msg2        db      'Ingenter', 10, 0
filename:   db      '/Users/trond/dev/assembly/test.txt', 0
filename2   db      '/Users/trond/dev/assembly/cprogram.c', 0

section .bss
fd_in       resb    1
buffer:     resb    16

section .text

global start

start:
;    pop     rdx
;    sub     rsp, 8 ; make up for popping argument
;    mov     rcx, 1
;    print_arg:          ; print all arguments
;        cmp     rdx, rcx
;        jle     noargs
;
;        mov     rdi, [rel rsp+8+(8*rcx)]
;        push    rcx
;        push    rdx
;        call    print_str_zeroterm
;        mov     rdi, 0x20 ; space
;        call    print_char
;        pop     rdx
;        pop     rcx
;        add     rcx, 1
;        jmp     print_arg
;    noargs:

    mov     rax, 0x2000005 ; Open file
    lea     rbx, [rel filename]
    mov     rcx, 0
    mov     rdx, 0777
    syscall
    jc      fileerror

    mov     rdi, rax
    call    print_num
    call    print_ln

    mov     rax, 0x2000003      ; Read file
    mov     rbx, 22            ; file descriptor
    mov     rcx, buffer
    mov     rdx, 4              ; # bytes to read
    syscall

    jc      fileerror

    mov     rdi, rax
    call    print_num
    call    print_ln

    mov     rdi, buffer
    mov     rsi, 4
    call    print_bytes
    call    print_ln

    mov     rdi, 1337
    call    print_num
    jmp     exit

    fileerror:
        mov     rdi, rax
        call    print_num
        call    print_ln
        mov     rdi, 0x0d
        call    print_num
        
    exit:
        call    print_ln
        mov     rax, 0x2000001 ; exit
        mov     rdi, 0
        syscall

start2:
    sub     rsp, 8
    push    rbp
    lea     rdi, [rel msg2]
    mov     rax, 0
    call    _printf
    pop     rbp
    add     rsp, 8

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

mess_with_buffer:
    mov     rax, 0x323539
    push    rax
    mov     [rel fd_in], rax
    mov     rdi, fd_in
    mov     rsi, 3
    call    print_bytes      
    call    print_ln