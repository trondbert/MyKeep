/usr/local/bin/nasm -f macho64 printing.asm;

/usr/local/bin/nasm -f macho64 my_assembly.asm;
ld my_assembly.o printing.o -lc -arch x86_64 -macosx_version_min 10.7.0 -no_pie -lSystem -o my_assembly64;

./my_assembly64
#./my_assembly64 "$@"

#if [ $# -gt 1 ]; then
#	./my_assembly64 "fd"
#else
#	./my_assembly64 fds
#fi
