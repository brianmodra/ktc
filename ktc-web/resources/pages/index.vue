<template>
    <div>
      <editor-content :editor="editor" class="editor" />
    </div>
  </template>
  
  <script setup>
  import { ref, onBeforeUnmount } from 'vue'
  import { Editor, EditorContent } from '@tiptap/vue-3'
  import StarterKit from '@tiptap/starter-kit'
  import { useWebSocket } from '@vueuse/core'
  
  const { data, status, send: sendMessage, close, open } = useWebSocket('ws://localhost:8889/websocket', {
    onError: (ws, event) => {
      console.error('WebSocket error:', event)
    },
    onConnected: (ws) => {
      console.log('Connected to WebSocket')
    },
    onDisconnected: (ws, event) => {
      console.log('Disconnected from WebSocket')
    }
  })

  const editor = ref(
    new Editor({
      content: '<p>The cat sat on the mat.</p>',
      extensions: [StarterKit],
      onUpdate({ editor }) {
      const data = editor.getJSON()
      handleContentChange(data)
    },
    })
  )
  
  onBeforeUnmount(() => {
    editor.value?.destroy()
  })

  function handleContentChange(data) {
    const content = data.content.filter(item => item.type === 'paragraph')
      .map(item => item.content? item.content.filter(text => text.type === 'text').map(text => text.text): [])
    console.log('Updated JSON:', JSON.stringify(content))
    if (status.value === 'OPEN') {
      sendMessage(JSON.stringify({ content: content }))
      console.log('Message sent successfully')
    } else {
      console.log('WebSocket not connected, cannot send message')
    }
  }
  </script>
  
  <style scoped>
  .editor {
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 1rem;
    min-height: 150px;
  }
  </style>