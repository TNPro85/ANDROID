PROJECT_ROOT_PATH := $(call my-dir)
LOCAL_PATH := $(PROJECT_ROOT_PATH)

include $(CLEAR_VARS)

LOCAL_SRC_FILES     := \
            native.c \
            md5.c \

LOCAL_C_INCLUDES    += $(LOCAL_PATH)

LOCAL_LDLIBS        := -llog
LOCAL_MODULE        := tnprocore
include $(BUILD_SHARED_LIBRARY)