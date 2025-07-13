#!/bin/bash
#
# Copyright (c) 2016-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
# Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
# Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
# Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
# Vestibulum commodo. Ut rhoncus gravida arcu.
#
BASE_DIR=`dirname $0`
python "${BASE_DIR}/rtp_analyzer.py" $@ --working_dir $BASE_DIR
