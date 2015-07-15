EXTERN int_to_str
EXTERN print_bytes
EXTERN print_char 
EXTERN print_ln
EXTERN print_num
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
 
section .text

global start
EXTERN _printf 

start:
    mov     rax, 0x2000074 ; 116 gettimeofday
    push    rbx
    mov     rdi, rsp
    mov     rsi, rsp
    syscall 
    pop     rdi
    mov     rdi, rax
    call    format_time
    jmp     end_start
    mov     rsi, rax
    lodsq
    mov     rdi, rax
    ;*********
    ;mov     rdi, rax
    call    print_num
    call    print_ln

    end_start:
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall

format_time: ; rdi holds epoch seconds
    push    r12
    appendYear:
    mov     rax, rdi
    mov     r12, 31536000 ; secs per year
    mov     rdx, 0        ; no fractions
    div     r12
    push    rdx             ; secs in this year
    add     rax, 1970       ; year
    mov     rdi, rax
    lea     rsi, [rel time_pretty]
    mov     rdx, 4
    call    int_to_str
    appendMonths:
    pop     rdx
    mov     rax, rdx
    mov     r12, 2592000    ; secs per month
    mov     rdx, 0
    div     r12
    push    rdx             ; secs in this month
    add     rax, 1
    mov     rdi, rax
    lea     rsi, [rel time_pretty+4] ; 4 første bytes er årstall
    mov     rdx, 2
    call    int_to_str
    appendDays:
    pop     rax
    mov     r12, 86400      ; secs per day
    mov     rdx, 0
    div     r12
    add     rax, 1
    mov     rdi, rax
    lea     rsi, [rel time_pretty+6] ; 6 første bytes er år og måned
    mov     rdx, 2
    call    int_to_str  

    lea     rdi, [rel time_pretty]
    call    print_str_zeroterm
    call    print_ln
    jmp     fmt_end

    fmt_end:
    pop     r12
    ret

old:
    add     rsp, 16 ; Skip argc and argv[0]
    pop     rsi
    sub     rbp, 8
    mov     rdi, rsi
    mov     rax, 0
    call    _printf  ; Printer argument
    add     rbp, 8
    mov     rdi, 10
    call    print_char

    ;;;;; printing meaning of life with printf
    sub     rbp, 16 ; 8 bytes x 2 (argument)
    mov     rax, 0
    lea     rdi, [rel fmt] ; RIP-relative
    mov     rsi, meaning
    call    _printf 
    sub     rbp, 16
    
	mov     rdi, msg
    mov     rsi, msg.len
    call 	print_bytes

	mov     rdi, buffer ; Hello world
    mov     rsi, buffer.len
    call 	print_bytes

    mov     rdi, 10
    call    print_char
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
    ret
