EXTERN mod
EXTERN print_bytes
EXTERN print_bytes_ln
EXTERN print_num
EXTERN print_num_ln
EXTERN print_ln
EXTERN print_str_zeroterm
EXTERN sqrt
EXTERN str_to_int


global start

section .data

msg_prime:      dw "Prime", 0
.len:           equ $ - msg_prime

msg_non_prime:  dw "Not prime", 0
.len:           equ $ - msg_non_prime

section .text

start:
    mov 	r15, [rsp+16]   ; input number
    mov 	rdi, r15
    call 	str_to_int 

    mov 	rdi, rax
    call 	sqrt 
    mov     r14, rax        ; sqrt number
    mov 	rdi, r15
    mov 	rsi, 2
    call 	mod 
    cmp     rax, 0
    jz      non_prime
    mov     rsi, 3

    each_divisor:           ; while divisor < input_number
        cmp     rsi, r14
        jg      prime           ; if divisor > sqrt
        mov 	rdi, r15
        mov 	rsi, rsi
        call 	mod 
        cmp     rax, 0
        jz      non_prime
        add     rsi, 2
        jmp     each_divisor

    non_prime:
        mov 	rdi, msg_non_prime
        mov 	rsi, msg_non_prime.len
        call 	print_bytes_ln 
        jmp     end_start

    prime:
       mov 	rdi, msg_prime
       mov 	rsi, msg_prime.len
       call 	print_bytes_ln 

    end_start:
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

