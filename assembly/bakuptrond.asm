; foo

section .data
    msg db 'This is a test', 10, 0    ; something stupid here

section .text
    global start
    extern _printf

start:
    push    rbp
    mov     rbp, rsp       

    xor     al, al ; ?
    lea     rdi, [rel msg] ; ?
    mov     rdi, msg
    call    _printf

    mov     rsp, rbp
    pop     rbp

    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall


    ret