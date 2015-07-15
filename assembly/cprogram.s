	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 10, 10
	.globl	_main
	.align	4, 0x90
_main:                                  ## @main
	.cfi_startproc
## BB#0:
	push	rbp
Ltmp0:
	.cfi_def_cfa_offset 16
Ltmp1:
	.cfi_offset rbp, -16
	mov	rbp, rsp
Ltmp2:
	.cfi_def_cfa_register rbp
	sub	rsp, 48
	lea	rax, qword ptr [rbp - 24]
	mov	dword ptr [rbp - 4], 0
	mov	dword ptr [rbp - 8], edi
	mov	qword ptr [rbp - 16], rsi
	mov	rdi, rax
	call	_time
	lea	rdi, qword ptr [rbp - 24]
	mov	qword ptr [rbp - 32], rax ## 8-byte Spill
	call	_ctime
	lea	rdi, qword ptr [rip + L_.str]
	mov	rsi, rax
	mov	al, 0
	call	_printf
	xor	ecx, ecx
	mov	dword ptr [rbp - 36], eax ## 4-byte Spill
	mov	eax, ecx
	add	rsp, 48
	pop	rbp
	ret
	.cfi_endproc

	.section	__TEXT,__cstring,cstring_literals
L_.str:                                 ## @.str
	.asciz	"%s"


.subsections_via_symbols
