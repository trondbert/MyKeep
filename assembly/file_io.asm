global start

EXTERN print_ln
EXTERN print_num_ln
EXTERN print_str_zeroterm
EXTERN print_bytes

section .text

start:
    ;read from file
    mov     rax, 0x2000005 ; open
    mov     rdi, file_name
    mov     rsi, 0
    mov     rdx, 0x0777
    syscall
    mov     r12, rax    ; file pointer
    mov     r13, fd_in

    mov     rax, 0x2000003 ; read
    mov     rdi, r12
    mov     rsi, fd_in
    mov     rdx, 255       ; nof bytes
    syscall

    mov     rdi, r12
    mov     rax, 0x2000006 ; close
    syscall

    new_byte:
    mov     rax, 0
    push    rax
    mov     rsi, r13            ; fra read files buffer
    mov     rdi, rsp            ; skriv til stacken
    movsb
    pop     rax
    cmp     rax, 0
    je      end_start           ; jump if equal

    mov     rax, 0x2000004  ; write
    mov     rdi, 1          ; stdout
    mov     rsi, r13
    mov     rdx, 1          ; antall bytes
    syscall
    inc     r13
    jmp     new_byte

    end_start:
        mov     rax, 0x2000001 ; exit
        mov     rdi, 0
        syscall

section	.data

file_name:      db './myfile.txt', 0

fd_in:          times 256 db 0
one_byte:       db 0
