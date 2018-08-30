package com.tompee.twitlet.base

import io.reactivex.Scheduler

data class Schedulers(val io: Scheduler,
                      val ui: Scheduler,
                      val computation: Scheduler)