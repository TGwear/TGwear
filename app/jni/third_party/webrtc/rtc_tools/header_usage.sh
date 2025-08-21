#!/bin/bash

#
# Copyright (c) 2016-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
# Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
# Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
# Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
# Vestibulum commodo. Ut rhoncus gravida arcu.
#

# This script is run in a git repository. It lists all header files,
# sorted by the number of other files where the file name of the file
# occurs. It is intentionally not limited to only source files, and
# there may be some false hits because we search only for the file
# part (sans directory). It is quite slow.
#
# Headers close to the top of the list are candidates for removal.

# If the name includes at most one directory, keep name unchanged,
# otherwise strip directories. Needed to work with relative #includes
# which are used in some parts of the tree, while still avoiding,
# e.g., api/foo.h to match includes of pc/foo.h.
simplify_name () {
  if expr "$1" : '.*/.*/' > /dev/null ; then
    basename "$1"
  else
    echo "$1"
  fi
}

git ls-files '*.h' '*.hpp' | while read header ; do
  name="$(simplify_name "${header}")"
  count="$(git grep -l -F  "${name}" \
           | grep -v -e '\.gn' -e '\.gyp'  \
           | wc -l)"
  echo "${count}" "${header}"
done | sort -n
