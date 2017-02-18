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

msg_fault:      dw "Fault:", 0
.len:           equ $ - msg_fault

msg_rsp:        dw "RSP: ", 0
.len:           equ $ - msg_rsp

section .text

start:
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

    mov     rax, 0x200006A  ; listen() 106 dec
    mov     rdi, r13
    mov     rsi, 0          ; connections queue size
    syscall

    push    QWORD 0
    push    QWORD 0
    mov     rsi, rsp
    push    DWORD 16  ; sockaddr is 16 bytes
    mov     rdx, rsp
    mov     rdi, r13
    mov     rax, 0x200001e ; accept() 30 dec
    syscall
    mov     r12, rax ; fd

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

    ;ssize_t sendto(int socket, const void *message, size_t length, int flags,
    ;  const struct sockaddr *dest_addr, socklen_t dest_len);
    mov     rax, 0x2000085 ; sendto() 133 dec
    mov     rdi, r12
    mov     rsi, msg_fault
    mov     rdx, msg_fault.len
    mov     r10, 8 ; end of record
    mov     r9, rsp
    add     r9, 8
    mov     r8, [rsp]
    syscall
    mov     rdi, rax
    call    print_num_ln

    mov     rax, 0x2000006 ; close socket fd
    mov     rdi, r13
    mov     rsi, 2
    syscall
    cmp     rax, 0
    jnz     err_returned

    mov     rax, 0x2000006
    mov     rdi, r12 ; fd incoming conn.
    mov     rsi, 2
    syscall
    cmp     rax, 0
    jnz     err_returned

    jmp     end_start

    err_returned:
    mov     rdi, msg_fault
    mov     rsi, msg_fault.len
    call    print_bytes_ln

    mov     rdi, rax
    call    print_num_ln
    mov     rbx, 0
    div     rbx

    end_start:
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
