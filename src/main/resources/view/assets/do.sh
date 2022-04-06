for file in *.png; do 
 convert $file -crop 184x229+8+22 processed/$file && echo $file;
done