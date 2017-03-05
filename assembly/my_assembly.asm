EXTERN sleep
EXTERN int_to_str
EXTERN print_bytes
EXTERN print_bytes_ln
EXTERN print_char
EXTERN print_raw
EXTERN print_ln
EXTERN print_num
EXTERN print_num_ln
EXTERN print_strlen
EXTERN print_str_zeroterm
EXTERN _printf

;;;;;;  RDI, RSI, RDX, RAX, RBC, RCX kan endres av kallet funksjon, kaller må evt. ta vare på dem. ;;;;;;

section .data 
msg:        dw      0x2134, 10 ; -> 0x34 0x20 -> ascii "4!"
.len:       equ     $ - msg

buffer:     db      'Hello, world'
                     times 64 - $ + buffer db ' '
.len:       equ     $ - buffer

numbers:    db      0x37, 0x34
meaning:    equ     42
msg_strlen: db      'String length: ', 0
wordvar:    dw      0x36, 0x38, 0

fmt2 db 'This is a test. %d is the meaning of life.', 10, 0    ; something stupid here
fmt  db '123456789', 10, 0    ; something stupid here

qbuf:           resq    0
time_pretty:    resq    0

animal2:  dw  'monkey', 0
animal:   dw  'elephant', 0

section .text

DEFAULT rel         ; [pointer+2] will always mean [rel pointer+2], not abs 

global start
EXTERN _printf

start:
    mov     rdi, [animal]
    push    rdi
    mov     rdi, rsp
    mov     rsi, 8
    call    print_bytes_ln
    pop     rax

    mov     rdi, 2
    call    sleep

    mov     rax, 0x2000074 ; 116 gettimeofday
    push    rbx
    mov     rsi, rsp
    push    rbx
    push    rbx
    mov     rdi, rsp
    syscall
    pop     rdi
    pop     rax
    pop     rax

    call    format_time

    end_start:
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

format_time: ; rdi holds epoch seconds
    push    r12
    call    format_time_append_year
    mov     rdi, rax
    call    format_time_append_months
    mov     rdi, rax
    call    format_time_append_days

    lea     rdi, [rel time_pretty]
    call    print_str_zeroterm
    call    print_ln
    pop     r12
    ret

format_time_append_year: ; writes year to destination and returns seconds in this year in rax
    mov     rax, rdi
    mov     r12, 31536000 ; secs per year
    mov     rdx, 0        ; no fractions
    div     r12
    push    rdx             ; secs in this year
    add     rax, 1970       ; year
    mov     rdi, rax
    lea     rsi, [rel time_pretty]
    mov     rcx, 4
    call    int_to_str
    
    pop     rax
    ret

format_time_append_months:  ; writes year to destination and returns seconds in this month in rax
    mov     rax, rdi
    mov     r12, 2592000    ; secs per month
    mov     rdx, 0
    div     r12
    push    rdx             ; secs in this month
    add     rax, 1
    mov     rdi, rax
    lea     rsi, [rel time_pretty] 
    add     rsi, 4                  ; 4 første bytes er årstall
    mov     rcx, 2
    call    int_to_str
    pop     rax
    ret

format_time_append_days:
    mov     rax, rdi
    mov     r12, 86400      ; secs per day
    mov     rdx, 0
    div     r12
    push    rdx
    add     rax, 1
    mov     rdi, rax
    lea     rsi, [rel time_pretty] 
    add     rsi, 6                  ; 6 første bytes er år og måned
    mov     rcx, 2
    call    int_to_str
    pop     rax
    ret
