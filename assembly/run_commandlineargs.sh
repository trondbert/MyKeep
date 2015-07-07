/usr/local/bin/nasm -f macho64 printing.asm;

/usr/local/bin/nasm -f macho64 commandlineargs.asm;
ld commandlineargs.o printing.o -lc -arch x86_64 -macosx_version_min 10.7.0 -no_pie -lSystem -o commandlineargs64;


./commandlineargs64 "$@"
