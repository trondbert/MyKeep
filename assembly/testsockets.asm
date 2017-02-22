EXTERN mod
EXTERN print_bytes
EXTERN print_bytes_ln
EXTERN print_num
EXTERN print_num_ln
EXTERN print_ln
EXTERN print_raw
EXTERN print_str_zeroterm
EXTERN sqrt
EXTERN str_to_int

global start

section .data

msg_fault:      dw "Fault: ", 0
.len:           equ $ - msg_fault

msg_to_client:  dw "Hello, how are you?", 10, 0
.len:           equ $ - msg_to_client

msg_rsp:        dw "RSP: ", 0
.len:           equ $ - msg_rsp

buffer:         times 256 db 0x2e
.len:           equ $ - buffer

section .text

start:
    mov     rax, 0x2000074 ; gettimeofday
    syscall
    mov     rdi, rax
    call    print_num_ln
    
    mov     rax, 0x2000061 ; 97 dec
    mov     rdi, 2 ; AF_INET
    mov     rsi, 1 ; SOCK_STREAM
    mov     rdx, 0 ; AF_UNSPEC
    syscall
    mov     r13, rax    ; listening socket

    ; building the sockaddr_in struct (sys/socket.h, netinet/in.h and bits/sockaddr.h)
    push 0              ; INADDR_ANY = 0 (uint32_t)
    push WORD 0x672b    ; port in byte reverse order = 11111 (uint16_t)
    push WORD 2         ; AF_INET = 2 (unsigned short int)
    
    mov     rax, 0x2000068  ; bind() 104 dec
    mov     rdi, r13
    mov     rsi, rsp        ; struct pointer
    mov     rdx, 16
    syscall
    cmp     rax, 0
    jnz     err_returned_bind

    mov     rax, 0x200006A  ; listen() 106 dec
    mov     rdi, r13
    mov     rsi, 0          ; connections queue size
    syscall

    push    QWORD 0x100100
    push    QWORD 0x0101
    push    QWORD 0x1010
    mov     rsi, rsp
    push    DWORD 16  ; sockaddr is 16 bytes
    mov     rdx, rsp
    mov     rdi, r13
    mov     rax, 0x200001e ; accept() 30 dec
    syscall
    mov     r12, rax ; fd

    mov     rax, 0x2000003 ; read r12 (client fd)
    mov     rdi, r12
    mov     rsi, buffer
    mov     rdx, 255
    syscall
    mov     rdi, rax
    call    print_num_ln
    mov     rdi, buffer
    mov     rsi, 255
    call    print_bytes_ln

    call    print_rsp

    mov     rax, 0x2000004 ; write r12 (client fd)
    mov     rdi, r12
    mov     rsi, msg_to_client
    mov     rdx, msg_to_client.len
    mov     rcx, 8 ; end of record
    mov     r9, rsp
    add     r9, 8
    mov     r8, 16
    syscall

    mov     rax, 0x2000086; shutdown
    mov     rdi, r12 ;incoming socket
    mov     rsi, 2 ; read and write
    syscall
    cmp     rax, 0
    jnz     err_returned_shutdown_client

    mov     rax, 0x2000006 ; close socket inc. conn
    mov     rdi, r12 ; fd incoming conn.
    mov     rsi, 2
    syscall
    cmp     rax, 0
    jnz     err_returned

    mov     rax, 0x2000006 ; close socket fd
    mov     rdi, r13
    mov     rsi, 2
    syscall
    cmp     rax, 0
    jnz     err_returned

    jmp     end_start

    err_returned_bind:
    mov     r15, 101
    jmp     err_returned
    err_returned_accept:
    mov     r15, 102
    jmp     err_returned
    err_returned_shutdown_client:
    mov     r15, 103
    jmp     err_returned
    err_returned_shutdown_server:
    mov     r15, 104
    jmp     err_returned
    err_returned:
    mov     rdi, msg_fault
    mov     rsi, msg_fault.len
    call    print_bytes_ln
    mov     rdi, r15    ; where it happened
    call    print_num_ln
    mov     rdi, rax    ; error code, often
    call    print_num_ln

    end_start:
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

print_rsp:
    mov     rdi, msg_rsp
    mov     rsi, msg_rsp.len
    call    print_bytes_ln
    mov     rdi, rsp
    call    print_raw
    mov     rdi, rsp
    add     rdi, 8
    call print_raw
    mov     rdi, rsp
    add     rdi, 16
    call print_raw
    mov     rdi, rsp
    add     rdi, 24
    call print_raw
    mov     rdi, rsp
    add     rdi, 32
    call print_raw
    mov     rdi, rsp
    add     rdi, 40
    call print_raw
    ret