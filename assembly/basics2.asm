; /usr/local/bin/nasm -f macho64 basics1.asm && ld -macosx_version_min 10.7.0 -lSystem -o basics1 basics1.o && ./basics1
 
global start
  
section .text
 
start:
    mov     rax, 2
    mov     rax, 0x2000001 ; exit
    mov     rdi, 0
    syscall
 
 
section .data
