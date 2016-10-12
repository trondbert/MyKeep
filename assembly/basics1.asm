; /usr/local/bin/nasm -f macho64 basics1.asm && ld -macosx_version_min 10.7.0 -lSystem -o basics1 basics1.o && ./basics1
 
global start
  
section .text
 
start:
    mov     rax, 0x0ccccccc
    mov     rbx, 0x0ddddddd
    mov     rdx, 0x0abcdefa
    mov     rsi, 0x0fffffff
    call 	callme
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
 
callme:    ; rdi in , rax out
	mov 	rax, 0x0aaaaaaa
    ret 
 
section .data
