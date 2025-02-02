import { createSlice } from '@reduxjs/toolkit'

export const counterSlice = createSlice({
  name: 'counter',
  initialState: {
    filenames: [],
  },
  reducers: {
    addFilenames: (state, action) => {
        console.log(action)
      state.filenames = action.payload
    },
  },
})


export const { addFilenames } = counterSlice.actions

export default counterSlice.reducer