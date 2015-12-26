
rm -f bin/file_io.o
rm -f bin/file_io

groovy compileNail.groovy file_io.nail file_io.asm &&
/usr/local/bin/nasm -f macho64 printing.asm -o bin/printing.o &&
/usr/local/bin/nasm -f macho64 file_io.asm -o bin/file_io.o &&
ld bin/file_io.o bin/printing.o -lc -arch x86_64 -macosx_version_min 10.7.0 -no_pie -lSystem -o bin/file_io &&
bin/file_io
