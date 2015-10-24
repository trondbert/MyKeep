
rm -f bin/prime.o
rm -f bin/prime

groovy compileNail.groovy prime.nail prime.asm &&
/usr/local/bin/nasm -f macho64 numbers.asm -o bin/numbers.o &&
/usr/local/bin/nasm -f macho64 printing.asm -o bin/printing.o &&
/usr/local/bin/nasm -f macho64 prime.asm -o bin/prime.o &&
/usr/local/bin/nasm -f macho64 prime.asm -o bin/prime.o &&
ld bin/prime.o bin/printing.o bin/numbers.o -lc -arch x86_64 -macosx_version_min 10.7.0 -no_pie -lSystem -o bin/prime64 &&
bin/prime64 $1 $2 $3
