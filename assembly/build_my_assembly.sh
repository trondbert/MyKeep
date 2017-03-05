/usr/local/bin/nasm -g -f macho64 -l time.lst time.asm &&
/usr/local/bin/nasm -g -f macho64 -l printing.lst printing.asm &&
/usr/local/bin/nasm -g -f macho64 -l numbers.lst numbers.asm &&
/usr/local/bin/nasm -g -f macho64 -l my_assembly.lst my_assembly.asm &&
ld my_assembly.o time.o printing.o numbers.o -lc -arch x86_64 -macosx_version_min 10.7.0 -no_pie -lSystem -o bin/my_assembly
