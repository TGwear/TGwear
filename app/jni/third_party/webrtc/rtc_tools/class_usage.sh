#!/bin/bash

#
# Copyright (c) 2017-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
# Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
# Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
# Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
# Vestibulum commodo. Ut rhoncus gravida arcu.
#

# This script is run in a git repository. It lists all classes defined
# in header files, sorted by the number of other files where the name
# of the class occurs. It is intentionally not limited to only source
# files. Classes close to the top of the list are candidates for
# removal.

git grep -h '^class .*[:{]' -- '*.h' '*.hpp' \
  | sed -e 's/WEBRTC_DLL_EXPORT// ' -e 's/^class *\([^ :{(<]*\).*/\1/' \
  | sort | uniq | while read class ; do
  count="$(git grep -l -w -F "${class}" | wc -l)"
  echo "${count}" "${class}"
done | sort -n
