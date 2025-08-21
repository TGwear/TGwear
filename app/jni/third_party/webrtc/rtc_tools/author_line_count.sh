#!/bin/bash

#
# Copyright (c) 2016-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
# Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
# Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
# Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
# Vestibulum commodo. Ut rhoncus gravida arcu.
#

# This script counts net line count contributions by author. Besides
# amusement, the value of these stats are of course questionable.

git log --pretty=format:%ae --shortstat "$@" \
  | sed '/^ /s/,/\n/g' \
  | gawk '
/^[^ ]/ {
  /* Some author "email addresses" have a trailing @svn-id, strip that out. */
  author = gensub(/^([^@]*@[^@]*).*/, "\\1", "g", $1);
}
/^ .*insertion/ { total[author] += $1 }
/^ .*deletion/ { total[author] -= $1 }
END { for (author in total) {
        print total[author], author
      }
}
' \
  | sort -nr
