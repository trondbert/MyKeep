global sleep

section .text

sleep: ; rdi is how many seconds to sleep
    push    r12             ; preserve
    push    r13             ; preserve
    push    r15             ; preserve
    mov     r15, rdi       ; how many seconds to sleep
    mov     rax, 0x2000074 ; 116 gettimeofday
    mov     rbx, 0
    push    rbx
    push    rbx         ; space for output
    mov     r12, rsp    ; location to store time
    mov     rdi, r12
    mov     rsi, 0      ; no time_zone
    
    syscall
    mov     rsi, r12
    lodsd
    mov     r13, rax    ; r13 is time to sleep from

    poll_time:
    mov     rax, 0x2000074 ; 116 gettimeofday
    mov     rdi, r12
    mov     rsi, 0          ; no time_zone
    syscall
    mov     rsi, r12
    lodsd
    sub     rax, r13
    cmp     rax, r15  ; time diff > input sleep seconds?
    jl      poll_time ; if not, repeat

    sleep_done:
    pop     rax
    pop     rax
    pop     r15         ; preserve
    pop     r13         ; preserve
    pop     r12         ; preserve
    ret
