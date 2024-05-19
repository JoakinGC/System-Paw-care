import React from 'react';
import {
  Document,
  Page,
  Text,
  StyleSheet,
  View,
  Image,
} from '@react-pdf/renderer';


const styles = StyleSheet.create({
  page: {
    backgroundColor: '#E4E4E4',
    padding: 30,
  },
  title: {
    fontSize: 24,
    textAlign: 'center',
    fontWeight: 'bold',
    marginBottom: 20,
  },
  section: {
    marginBottom: 10,
  },
  label: {
    fontSize: 12,
    fontWeight: 'bold',
  },
  value: {
    fontSize: 12,
    marginBottom: 5,
  },
  logo: {
    width: 100,
    height: 100,
    marginBottom: 20,
    alignSelf: 'center',
  },
  pageNumber: {
    position: 'absolute',
    fontSize: 12,
    bottom: 30,
    left: 0,
    right: 0,
    textAlign: 'center',
    color: 'grey',
  },
});

const CitaPDF = ({ fechaCita, motivo, nombreSolicitante }) => (
  <Document>
    <Page style={styles.page}>
      <Text style={styles.title}>Detalles de la Cita</Text>
      <View style={styles.section}>
        <Text style={styles.label}>Fecha de Cita:</Text>
        <Text style={styles.value}>{fechaCita}</Text>
      </View>
      <View style={styles.section}>
        <Text style={styles.label}>Motivo:</Text>
        <Text style={styles.value}>{motivo}</Text>
      </View>
      <View style={styles.section}>
        <Text style={styles.label}>Nombre del Solicitante:</Text>
        <Text style={styles.value}>{nombreSolicitante}</Text>
      </View>
      <Text
        style={styles.pageNumber}
        render={({ pageNumber, totalPages }) => `${pageNumber} / ${totalPages}`}
      />
    </Page>
  </Document>
);

export default CitaPDF;
