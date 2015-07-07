section .data
    msg db 'This is a test', 10, 0    ; something stupid here

section .text
    global start
    extern _printf

start:
    sub     rbp, 8

    ;mov     rdi, msg
    lea     rdi, [rel msg]
    call    _printf

    add     rbp, 8

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
 

    ret