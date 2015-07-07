section .data
    msg db 'This is a test', 10, 0    ; something stupid here

section .text
    global start
    extern _printf

start:
    mov     rdi, msg
    call    _printf

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
 

    ret